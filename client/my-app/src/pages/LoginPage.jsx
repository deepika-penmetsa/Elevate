import { useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { userAuth, fetchUserData } from "../services/api";
import { setUser, setUserClubs } from "../redux/userSlice";
import styles from "../styles/LoginPage.module.css";
import elevateLogo from "../assets/elevate-logo.svg";
import { FaEye, FaEyeSlash } from "react-icons/fa";

function LoginPage() {
  const [formData, setFormData] = useState({ email: "user1@gmail.com", password: "12345678" });
  const [error, setError] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");
    try {
      const loginResponse = await userAuth(formData);
      if (loginResponse.error) {
        setError("Invalid email or password");
        return;
      }

      const userData = await fetchUserData(formData.email);
      if (userData.error) {
        setError("Error fetching user data");
        return;
      }
      const userDetails = userData.data[0];
      dispatch(setUser(userDetails));
      dispatch(setUserClubs(userDetails.userClubs.map((c) => c.club)));

      navigate("/dashboard");
    } catch (error) {
      setError(error.response?.data?.message || "Login failed");
    }
  };

  return (
    <div className={styles.container}>
      {/* Left Section - Branding */}
      <div className={styles.leftSection}>
        <div className={styles.logoContainer}>
        <img src={elevateLogo} alt="Elevate" className={styles.brandImage} />
        </div>
      </div>
      {/* Right Section - Login Form */}
      <div className={styles.rightSection}>
        <form onSubmit={handleLogin} className={styles.form}>
          <label className={styles.label}>User ID</label>
          <input
            type="email"
            name="email"
            placeholder="Your Email ID"
            className={styles.input}
            value={formData.email}
            onChange={handleChange}
            required
          />
          <label className={styles.label}>Password</label>
          <div className={styles.passwordContainer}>
            <input
              type={showPassword ? "text" : "password"}
              name="password"
              placeholder="*****"
              className={styles.input}
              value={formData.password}
              onChange={handleChange}
              required
            />
            <span className={styles.eyeIcon} onClick={() => setShowPassword(!showPassword)}>
              {showPassword ? <FaEyeSlash /> : <FaEye />}
            </span>
          </div>
          {error && <p className={styles.error}>{error}</p>}
          <button type="submit" className={styles.button}>LOGIN</button>
        </form>
        <div className={styles.links}>
          <p className={styles.link} onClick={() => navigate("/forgot-password")}>Forgot your password?</p>
          <p className={styles.link} onClick={() => navigate("/signup")}>Create a new account</p>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;