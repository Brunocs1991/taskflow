const API = import.meta.env.VITE_API_URL;

async function http(path, options = {}) {
  const res = await fetch(`${API}${path}`, {
    headers: { "Content-Type": "application/json", ...(options.headers || {}) },
    ...options,
  });

  const isJson = res.headers.get("content-type")?.includes("application/json");
  const body = isJson ? await res.json() : await res.text();

  if (!res.ok) {
    const message =
      (body && body.message) ||
      (typeof body === "string" && body) ||
      `HTTP ${res.status}`;
    const error = new Error(message);
    error.status = res.status;
    error.body = body;
    throw error;
  }

  return body;
}

export const taskApi = {
  list: ({ page = 0, size = 10, status, q } = {}) => {
    const params = new URLSearchParams({
      page: String(page),
      size: String(size),
    });
    if (status) params.set("status", status);
    if (q) params.set("q", q);
    return http(`/api/tasks?${params.toString()}`);
  },

  get: (id) => http(`/api/tasks/${id}`),

  create: (payload) =>
    http(`/api/tasks`, { method: "POST", body: JSON.stringify(payload) }),

  update: (id, payload) =>
    http(`/api/tasks/${id}`, { method: "PUT", body: JSON.stringify(payload) }),

  remove: (id) => http(`/api/tasks/${id}`, { method: "DELETE" }),

  listComments: (taskId) => http(`/api/tasks/${taskId}/comments`),

  addComment: (taskId, payload) =>
    http(`/api/tasks/${taskId}/comments`, {
      method: "POST",
      body: JSON.stringify(payload),
    }),
};
