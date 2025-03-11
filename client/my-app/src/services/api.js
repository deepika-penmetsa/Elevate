const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;
const fetchAPI = async (URL, method = 'GET', data = null, authRequired = false) => {
    try {
        const token = localStorage.getItem('jwtToken');  // Get token before request
        
        const options = {
            method,
            headers: {
                'Content-Type': 'application/json',
            },
        };

        // Attach Authorization header only if token exists
        if (authRequired && token) {
            options.headers['Authorization'] = `Bearer ${token}`;
        }

        if (data) {
            options.body = JSON.stringify(data);
        }

        const response = await fetch(`${API_BASE_URL}${URL}`, options);
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return await response.json();

    } catch (error) {
        console.error("API Error:", error);
        return { error: error.message };
    }
};

// User API
export const createUser = (data) => fetchAPI('/api/user', 'POST', data);
export const userAuth = async (data) =>  {
    const response = await fetchAPI('/auth/login', 'POST', data);
    if(response.token) {
        localStorage.setItem('jwtToken', response.token);
    }
    return response
}
export const getUserClubs = (id) => fetchAPI(`/api/user/${id}`, 'GET');
export const fetchUserData = (email) => fetchAPI(`/student/users/email?email=${email}`, 'GET', null, true);
export const fetchUserClubRequests = (userId) => fetchAPI(`/student/club-requests/user/${userId}`, 'GET', null, true);

// Clubs API
export const fetchAllClubs = () => fetchAPI("/student/clubs", "GET", null, true);
export const requestToJoinClub = (clubName) => fetchAPI("/api/club-requests", "POST", { clubName }, true);
// Announcements API
export const fetchAnnouncements = (clubId) => fetchAPI(`/api/announcements/${clubId}`, 'GET', null, true);