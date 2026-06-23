# Passit deployment: ECR images + EC2 + RDS + AI server on AWS

This deployment layout is intended for:

- `frontend` image pushed to Amazon ECR and pulled on one EC2 instance
- `backend` image pushed to Amazon ECR and pulled on the same EC2 instance
- `ai-server` image pushed to Amazon ECR and pulled on the same EC2 instance
- `MySQL` running on Amazon RDS

## Architecture

Traffic flow:

- user -> EC2 `:80` -> frontend container
- frontend -> `/api/*` -> backend container `:8080`
- backend -> ai-server container `:8000`
- backend -> RDS MySQL `:3306`
- ai-server -> OpenAI-compatible API via `OPENAI_BASE_URL`

## Why this layout

- Your AI server uses OpenAI-compatible remote inference, so it does not require a GPU instance.
- Keeping AI on AWS avoids exposing your local machine to EC2 and removes local uptime dependency.
- Using ECR means GitHub Actions ships Docker images instead of rebuilding app source directly on EC2.
- RDS still keeps the EC2 host lighter than running MySQL in Docker on the same machine.

## Recommended starting size

With about `$140` in AWS credits, start simple:

- EC2: `t3.medium` if usage is light, `t3.large` if you expect judge usage and AI requests together
- RDS: `db.t3.micro` Single-AZ MySQL 8

This is an inference based on your stack shape, not a quoted AWS price. Region affects cost, so confirm with AWS Pricing Calculator before launch.

## 1. RDS

Create an RDS MySQL database named `passit`.

Recommended settings:

- engine: MySQL 8
- public access: off
- Single-AZ
- allow inbound `3306` only from the EC2 security group

On first application start, Flyway runs the migration in `src/main/resources/db/migration/V1__init.sql`.

## 2. EC2

Create one EC2 instance for frontend, backend, and ai-server.

Recommended inbound rules:

- `22` from your IP
- `80` from the internet

Install Docker Engine, Docker Compose plugin, and AWS CLI, then prepare directories:

```bash
sudo mkdir -p /opt/passit/backend
sudo mkdir -p /opt/passit/ai/chroma_db
sudo mkdir -p /opt/passit/judge-workspaces
sudo chown -R $USER:$USER /opt/passit
```

Judge execution note:

- the backend container does not compile Java, C++, or Python inside itself
- instead, it talks to the EC2 host Docker daemon through `/var/run/docker.sock`
- each submission starts a separate runtime container for Python, Java, or C++
- the host path `/opt/passit/judge-workspaces` is mounted into both the backend container and each judge runtime container

Default judge runtime images:

- Python: `python:3.11-slim`
- Java: `eclipse-temurin:21`
- C++: `gcc:13`

You can override them in `.env.aws` with:

- `JUDGE_PYTHON_IMAGE`
- `JUDGE_JAVA_IMAGE`
- `JUDGE_CPP_IMAGE`

For a steadier production setup, mirror those three images into ECR and point the variables at your ECR image URIs. That avoids first-run pulls from Docker Hub and reduces Docker Hub rate-limit risk.

## 3. ECR and IAM

Create or allow creation of these ECR repositories:

- `passit-frontend`
- `passit-backend`
- `passit-ai-server`

The GitHub Actions workflows use OIDC to assume an AWS role and push images to ECR.

GitHub’s official OIDC guide says to use the GitHub OIDC provider URL `https://token.actions.githubusercontent.com`, audience `sts.amazonaws.com`, and a trust policy that restricts the `sub` claim to your repository. GitHub also notes you need `permissions.id-token: write` in the workflow. [GitHub Docs](https://docs.github.com/en/actions/how-tos/secure-your-work/security-harden-deployments/oidc-in-aws)

The ECR login action’s official example uses `aws-actions/configure-aws-credentials@v4` followed by `aws-actions/amazon-ecr-login@v2` before building and pushing Docker images. [aws-actions/amazon-ecr-login](https://github.com/aws-actions/amazon-ecr-login)

Attach an IAM role to the EC2 instance with ECR pull permission, such as AmazonEC2ContainerRegistryReadOnly.

## 4. AI server data

The AI repository currently has a local `chroma_db/chroma.sqlite3` file that is not tracked in Git.

Before first deploy, copy your local Chroma data to the EC2 host:

```bash
scp -i <your-key>.pem -r ./chroma_db/* <user>@<ec2-host>:/opt/passit/ai/chroma_db/
```

The Compose stack mounts `/opt/passit/ai/chroma_db` into the ai-server container.

## 5. Environment file

Create the file:

```bash
cp .env.aws.example .env.aws
```

Then fill in:

- RDS endpoint and credentials
- `AWS_REGION`
- strong `JWT_SECRET`
- admin credentials
- `OPENAI_API_KEY`
- optional `OPENAI_BASE_URL`, model names, and RAG tuning values

Because `deploy-stack.sh` sources `.env.aws` as a shell file, any value containing spaces must be quoted.

Example:

```env
JAVA_OPTS="-Xms512m -Xmx1024m"
```

## 6. First deployment order

For the first deployment, run the backend workflow once first, then run the AI server workflow once.

Why:

- the backend workflow uploads the deploy script and Compose file to EC2
- the ai-server workflow writes `.env.images.ai` and then runs the first full deployment

After that, either repository can deploy independently.

## 7. Start the stack manually if needed

```bash
cd /opt/passit/backend
bash scripts/deploy-stack.sh
```

The deploy script logs into ECR with `aws ecr get-login-password` and then pulls the images. Amazon ECR’s registry auth guide documents this exact pattern. [Amazon ECR docs](https://docs.aws.amazon.com/AmazonECR/latest/userguide/registry_auth.html)

## 8. GitHub Actions

Backend repository workflows:

- `.github/workflows/ci.yml`
- `.github/workflows/deploy-ec2.yml`

AI server repository workflows:

- `.github/workflows/ci.yml`
- `.github/workflows/deploy-ec2.yml`

Backend deploy workflow:

- builds backend and frontend images
- pushes them to ECR
- uploads only deployment assets to EC2
- writes `.env.images.backend`
- runs the deploy script only if `.env.images.ai` already exists

AI server deploy workflow:

- builds ai-server image
- pushes it to ECR
- writes `.env.images.ai`
- runs the same deploy script

Required GitHub repository secrets:

- `AWS_ROLE_TO_ASSUME`
- `EC2_HOST`
- `EC2_USER`
- `EC2_SSH_PRIVATE_KEY`
- `EC2_DEPLOY_ROOT`
- `PASSIT_ENV_AWS`

`PASSIT_ENV_AWS` should contain the full multi-line contents of the production `.env.aws` file.

This lets GitHub Actions rewrite `/opt/passit/backend/.env.aws` on every deploy so EC2 does not drift away from the repository and secret state.

Recommended GitHub repository variable:

- `AWS_REGION`

Recommended deploy root:

```text
/opt/passit
```

## 7. Credit and plan note

AWS currently says new customers can receive up to `$200` in credits and explore AWS at no cost for up to 6 months, and those credits can still apply after upgrading to a paid plan within that window.

If you want this environment to stay alive without the free-plan account closure behavior, use a paid plan and let the remaining credits offset the bill.
