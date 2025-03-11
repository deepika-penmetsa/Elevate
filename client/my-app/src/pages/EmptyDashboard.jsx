import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { fetchAllClubs } from "../services/api";
import GreetingBox from "../components/GreetingBox";
import ClubCard from "../components/ClubCard";
import styles from "../styles/Dashboard.module.css";
import emptyImage from "../assets/empty.svg";

const EmptyDashboard = () => {
  const user = useSelector((state) => state.user.user);
  const [clubs, setClubs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  useEffect(() => {
    console.log("user ",user)
    const getClubs = async () => {
      try {
        const response = await fetchAllClubs();
        if (response.error) throw new Error(response.error);
        setClubs(response.data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };
    getClubs();
  }, []);

  return (
    <div className={styles.dashboardContainer}>
      <GreetingBox />
      <div className={styles.emptyState}>
        <img src={emptyImage} alt="Empty State" className={styles.emptyImage} />
        <div className={styles.emptyText}>
        <h2 className={styles.emptyTitle}>Looks Empty Here...</h2>
        <p className={styles.emptySubtitle}>Join your first club</p>
        </div>
      </div>
      <h3 className={styles.exploreTitle}>Explore</h3>
      {loading ? (
        <p className={styles.loadingText}>Loading clubs...</p>
      ) : error ? (
        <p className={styles.errorText}>Failed to load clubs: {error}</p>
      ) : (
        <div className={styles.clubGrid}>
          {clubs.map((club) => (
            <ClubCard key={club.clubName} club={club} />
          ))}
        </div>
      )}
    </div>
  );
};

export default EmptyDashboard;
