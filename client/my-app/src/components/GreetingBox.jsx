import { useSelector } from "react-redux";
import styles from "../styles/GreetingBox.module.css";

const GreetingBox = () => {
  const user = useSelector((state) => state.user.user);

  return (
    <div className={styles.greetingBox}>
      <h2 className={styles.greetingText}>Welcome, {user?.firstName || "User"}!</h2>
      <div className={styles.userProfile}>
        <span className={styles.userName}>{user?.firstName}</span>
        <div className={styles.profileIcon}></div> {/* Placeholder profile icon */}
      </div>
    </div>
  );
};

export default GreetingBox;