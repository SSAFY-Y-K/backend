const BASE = "/api";

async function request(method, path, body) {
	const res = await fetch(BASE + path, {
		method,
		headers: { "Content-Type": "application/json" },
		body: body ? JSON.stringify(body) : undefined,
	});
	if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
	if (res.status === 204) return null;
	return res.json();
}

export const api = {
	get: (path) => request("GET", path),
	post: (path, body) => request("POST", path, body),
	put: (path, body) => request("PUT", path, body),
	delete: (path) => request("DELETE", path),
};

// Posts
export const getPosts = () => api.get("/posts");
export const getPostDetail = (id) => api.get(`/posts/${id}`);
export const createPost = (body) => api.post("/posts", body);
export const updatePost = (id, body) => api.put(`/posts/${id}`, body);
export const deletePost = (id) => api.delete(`/posts/${id}`);

// Coding problems
export const getCodingProblems = () => api.get("/problems/algorithm");
export const getCodingProblemDetail = (id) => api.get(`/problems/algorithm/${id}`);
export const generateCodingProblem = (body) => api.post("/problems/algorithm/generate", body);
export const submitCode = (problemId, body) => api.post(`/problems/${problemId}/submissions`, body);
export const getSubmission = (id) => api.get(`/submissions/${id}`);
