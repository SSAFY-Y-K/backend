#!/usr/bin/env bash
set -euo pipefail

PASSIT_ROOT="${PASSIT_ROOT:-/opt/passit}"
BACKEND_DIR="$PASSIT_ROOT/backend"
ENV_FILE="${ENV_FILE:-.env.aws}"
BACKEND_IMAGE_ENV_FILE="${BACKEND_IMAGE_ENV_FILE:-.env.images.backend}"
AI_IMAGE_ENV_FILE="${AI_IMAGE_ENV_FILE:-.env.images.ai}"

if [[ ! -d "$BACKEND_DIR" ]]; then
  echo "backend directory not found: $BACKEND_DIR" >&2
  exit 1
fi

cd "$BACKEND_DIR"

if [[ ! -f "$ENV_FILE" ]]; then
  echo "env file not found: $BACKEND_DIR/$ENV_FILE" >&2
  exit 1
fi

set -a
. "$ENV_FILE"
if [[ -f "$BACKEND_IMAGE_ENV_FILE" ]]; then
  . "$BACKEND_IMAGE_ENV_FILE"
fi
if [[ -f "$AI_IMAGE_ENV_FILE" ]]; then
  . "$AI_IMAGE_ENV_FILE"
fi
set +a

if [[ -z "${AWS_REGION:-}" ]]; then
  echo "AWS_REGION is required in $ENV_FILE" >&2
  exit 1
fi

if [[ -z "${FRONTEND_IMAGE:-}" || -z "${BACKEND_IMAGE:-}" || -z "${AI_SERVER_IMAGE:-}" ]]; then
  echo "FRONTEND_IMAGE, BACKEND_IMAGE, and AI_SERVER_IMAGE must be set before deployment." >&2
  exit 1
fi

REGISTRY="${BACKEND_IMAGE%%/*}"
aws ecr get-login-password --region "$AWS_REGION" | docker login --username AWS --password-stdin "$REGISTRY"

docker compose -f docker-compose.aws.yml pull
docker compose -f docker-compose.aws.yml up -d --remove-orphans
