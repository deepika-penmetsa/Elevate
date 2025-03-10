import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { Provider, useSelector } from "react-redux";
import { store } from "./redux/store";
import LandingPage from "./pages/LandingPage.jsx";
import LoginPage from "./pages/LoginPage.jsx";
import SignupPage from "./pages/SignupPage.jsx";
import Sidebar from "./components/Sidebar";
import EmptyDashboard from "./pages/EmptyDashboard";
import DashboardWithClubs from "./pages/DashboardWithClubs"; // Import the new dashboard

function PrivateRoute({ children }) {
  const token = localStorage.getItem("jwtToken");
  return token ? children : <Navigate to="/login" />;
}

function DashboardWrapper() {
  const userClubs = useSelector((state) => state.user.userClubs);
  console.log("user clubs from app.jsx",userClubs)
  return userClubs.length > 0 ? <DashboardWithClubs /> : <EmptyDashboard />;
}

function App() {
  return (
    <Provider store={store}>
      <Router>
        <Routes>
          {/* Public Routes */}
          <Route path="/" element={<LandingPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignupPage />} />

          {/* Protected Routes */}
          <Route
            path="/dashboard"
            element={
              <PrivateRoute>
                <ProtectedLayout>
                  <DashboardWrapper />
                </ProtectedLayout>
              </PrivateRoute>
            }
          />
        </Routes>
      </Router>
    </Provider>
  );
}

function ProtectedLayout({ children }) {
  return (
    <div style={{ display: "flex", width: "100vw", height: "100vh", overflow: "hidden" }}>
      <Sidebar />
      <div style={{ flexGrow: 1, padding: "10px", marginLeft: "210px", overflowY: "hidden" }}>
        {children}
      </div>
    </div>
  );
}

export default App;