import { useSelector } from "react-redux";
import Calendar from "../components/Calendar";
import ExploreClubs from "../components/ExploreClubs";
import Requests from "../components/Requests";
import ClubCardWithClubs from "../components/ClubCardWithClubs";
import styles from "../styles/DashboardWithClubs.module.css";

const DashboardWithClubs = () => {
  const userClubs = useSelector((state) => state.user.userClubs);

  return (
    <div className={styles.dashboardContainer}>
      {/* Header Section */}
      <div className={styles.header}>
        <h1 className={styles.title}>Dashboard</h1>
        <div className={styles.profileContainer}>
          <span className={styles.username}>Ranjith</span>
          <div className={styles.profileIcon}></div>
        </div>
      </div>

      {/* Main Content Section */}
      <div className={styles.mainContent}>
        {/* Club List */}
        <div className={styles.clubSection}>
          <h2 className={styles.sectionTitle}>My Clubs</h2>
          <div className={styles.clubGrid}>
            {userClubs.map((club) => (
              <ClubCardWithClubs key={club.clubId} club={club} />
            ))}
          </div>
        </div>

        {/* Right Sidebar */}
        <div className={styles.sidebar}>
          <Calendar />
          <ExploreClubs />
          <Requests userId={13}/>
        </div>
      </div>
    </div>
  );
};

export default DashboardWithClubs;