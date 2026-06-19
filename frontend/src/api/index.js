import { authApi, publicApi } from "@/api/client";

// Posts
export const getPosts = () => publicApi.get("/posts");
export const getPostDetail = (id) => publicApi.get(`/posts/${id}`);
export const createPost = (body) => authApi.post("/posts", body);
export const updatePost = (id, body) => authApi.put(`/posts/${id}`, body);
export const deletePost = (id) => authApi.delete(`/posts/${id}`);

// Coding problems
export const getCodingProblems = () => publicApi.get("/problems/algorithm");
export const getCodingProblemDetail = (id) => publicApi.get(`/problems/algorithm/${id}`);
export const generateCodingProblem = (body) => authApi.post("/problems/algorithm/generate", body);
export const deleteCodingProblem = (id) => authApi.delete(`/problems/algorithm/${id}`);
export const submitCode = (problemId, body) => authApi.post(`/problems/${problemId}/submissions`, body);
export const getSubmission = (id) => authApi.get(`/submissions/${id}`);

// My page
export const getMyProfile = () => authApi.get("/users/me");
export const getMyPosts = () => authApi.get("/users/me/posts");
export const getMySubmissions = () => authApi.get("/users/me/submissions");
export const logout = () => publicApi.post("/auth/logout");
