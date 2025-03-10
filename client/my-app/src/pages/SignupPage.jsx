import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createUser } from "../services/api"; // Import API function
import styles from "../styles/SignupPage.module.css";

function SignupPage() {
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    phone: "",
    address: "",
    role: "STUDENT",
    birthday: "",
  });
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSignup = async (e) => {
    e.preventDefault();
    try {
      await createUser(formData);
      alert("Signup successful! Redirecting to login...");
      navigate("/login");
    } catch (error) {
      setError(error.response?.data?.message || "Signup failed");
    }
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.title}>Sign Up</h2>
      {error && <p className={styles.error}>{error}</p>}
      <form onSubmit={handleSignup} className={styles.form}>
        <input type="text" name="firstName" placeholder="First Name" className={styles.input} value={formData.firstName} onChange={handleChange} required />
        <input type="text" name="lastName" placeholder="Last Name" className={styles.input} value={formData.lastName} onChange={handleChange} required />
        <input type="email" name="email" placeholder="Email" className={styles.input} value={formData.email} onChange={handleChange} required />
        <input type="password" name="password" placeholder="Password" className={styles.input} value={formData.password} onChange={handleChange} required />
        <input type="tel" name="phone" placeholder="Phone" className={styles.input} value={formData.phone} onChange={handleChange} required />
        <input type="text" name="address" placeholder="Address" className={styles.input} value={formData.address} onChange={handleChange} required />
        <input type="date" name="birthday" className={styles.input} value={formData.birthday} onChange={handleChange} required />
        <button type="submit" className={styles.button}>Sign Up</button>
      </form>
      <p className={styles.link} onClick={() => navigate("/login")}>Already have an account? Login</p>
    </div>
  );
}

export default SignupPage;