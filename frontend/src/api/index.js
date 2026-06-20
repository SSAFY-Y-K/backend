import { authApi, publicApi } from "@/api/client";

// Posts
export const getPosts = (params) => publicApi.get("/posts", { params });
export const getPostDetail = (id) => publicApi.get(`/posts/${id}`);
export const createPost = (body) => authApi.post("/posts", body);
export const updatePost = (id, body) => authApi.put(`/posts/${id}`, body);
export const deletePost = (id) => authApi.delete(`/posts/${id}`);

// Comments
export const getComments = (postId) => publicApi.get(`/posts/${postId}/comments`);
export const createComment = (postId, body) => authApi.post(`/posts/${postId}/comments`, body);
export const deleteComment = (postId, commentId) => authApi.delete(`/posts/${postId}/comments/${commentId}`);

// Coding problems
export const getCodingProblems = () => publicApi.get("/problems/algorithm");
export const getCodingProblemDetail = (id) => publicApi.get(`/problems/algorithm/${id}`);
export const generateCodingProblem = (body) => authApi.post("/problems/algorithm/generate", body);
export const deleteCodingProblem = (id) => authApi.delete(`/problems/algorithm/${id}`);
export const submitCode = (problemId, body) => authApi.post(`/problems/${problemId}/submissions`, body);
export const runCode = (problemId, body) => authApi.post(`/problems/${problemId}/run`, body);

// Reports
export const submitReport = (problemId, body) => authApi.post(`/problems/${problemId}/reports`, body);
export const getAllReports = () => authApi.get('/admin/reports');
export const getReports = (problemId) => authApi.get(`/admin/problems/${problemId}/reports`);
export const resolveReport = (reportId) => authApi.patch(`/admin/reports/${reportId}/resolve`);
export const getAllTestCases = (problemId) => authApi.get(`/admin/problems/${problemId}/test-cases`);
export const updateTestCase = (testCaseId, body) => authApi.put(`/admin/test-cases/${testCaseId}`, body);
export const getSubmission = (id) => authApi.get(`/submissions/${id}`);

// My page
export const getMyProfile = () => authApi.get("/users/me");
export const getMyPosts = () => authApi.get("/users/me/posts");
export const getMySubmissions = () => authApi.get("/users/me/submissions");
export const getMyStats = () => authApi.get("/users/me/stats");
export const updateProfile = (body) => authApi.put("/users/me", body);
export const logout = () => publicApi.post("/auth/logout");
