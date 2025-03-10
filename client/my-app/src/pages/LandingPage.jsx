import { Link } from "react-router-dom";
import styles from "../styles/LandingPage.module.css";

function LandingPage() {
  return (
    <div className={styles.container}>
      <h1 className={styles.title}>Welcome to College Club Platform</h1>
      <div className={styles.buttonGroup}>
        <Link to="/login" className={styles.loginButton}>Login</Link>
        <Link to="/signup" className={styles.signupButton}>Sign Up</Link>
      </div>
    </div>
  );
}

export default LandingPage;
