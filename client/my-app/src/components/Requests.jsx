import { useEffect, useState } from "react";
import { fetchUserClubRequests } from "../services/api";
import styles from "../styles/Requests.module.css";

const Requests = ({ userId }) => {
  const [requests, setRequests] = useState([]);
  const [expanded, setExpanded] = useState(false);
  useEffect(() => {
    const fetchRequests = async () => {
      const response = await fetchUserClubRequests(userId);
      if (response.data) {
        setRequests(response.data);
      }
    };
    fetchRequests();
  }, [userId]);

  const handleWithdraw = (clubId) => {
    console.log(`Withdraw request for club ID: ${clubId}`);
    setRequests(requests.filter((req) => req.clubId !== clubId));
  };

  const handleReapply = (clubId) => {
    console.log(`Reapply request for club ID: ${clubId}`);
    // API call logic to reapply (not implemented yet)
  };

  return (
    <div className={styles.requestsContainer}>
      <p className={styles.title}>Requests</p>
      <div className={styles.requestsGrid} style={{ maxHeight: expanded ? "none" : "200px" }}>
        {requests.map((request, index) => (
          <div key={index} className={styles.requestCard}>
            <h3>{request.clubName}</h3>
            <p className={styles.status}>{request.requestStatus}</p>
            {request.requestStatus === "PENDING" && <button className={styles.actionButton}>Withdraw</button>}
            {request.requestStatus === "REJECTED" && <button className={styles.actionButton}>Reapply</button>}
          </div>
        ))}
      </div>
      {!expanded && (
        <button className={styles.showMore} onClick={() => setExpanded(true)}>Show More</button>
      )}
    </div>
  );
};

export default Requests;