import { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import { Link, useLocation } from "react-router-dom";
import styles from "../styles/Sidebar.module.css";
import { FaChevronDown, FaChevronRight } from "react-icons/fa";
import { MdDashboard, MdExplore } from "react-icons/md";
import { SiStatuspage } from "react-icons/si";
import { HiOutlineSpeakerphone, HiOutlineClipboardList } from "react-icons/hi";
import { RiLogoutCircleRLine } from "react-icons/ri";
import { BsCalendarEvent } from "react-icons/bs";
import { GoCommentDiscussion } from "react-icons/go";
import { TbProgressBolt } from "react-icons/tb";
const Sidebar = () => {
  const location = useLocation();
  const userClubs = useSelector((state) => state.user.userClubs);
  const [expandedClubs, setExpandedClubs] = useState({});

  useEffect(() => {
    console.log("user data from sidebar ", userClubs);
  }, []);

  const toggleClubMenu = (clubId) => {
    setExpandedClubs((prev) => ({
      ...prev,
      [clubId]: !prev[clubId], // Toggle the club menu
    }));
  };

  const handleLogout = () => {
    localStorage.removeItem("jwtToken");
    window.location.href = "/login";
  };

  return (
    <div className={styles.sidebar}>
      <div className={styles.header}>
        <h2 className={styles.logo}>Elevate</h2>
      </div>

      <nav className={styles.nav}>
        <Link to="/dashboard" className={location.pathname === "/dashboard" ? styles.active : ""}>
          <span className={styles.icon}><MdDashboard /></span> Dashboard
        </Link>
        <Link to="/explore" className={location.pathname === "/explore" ? styles.active : ""}>
          <span className={styles.icon}><MdExplore /></span> Explore
        </Link>
        <Link to="/requests" className={location.pathname === "/requests" ? styles.active : ""}>
          <span className={styles.icon}><TbProgressBolt /></span> Requests
        </Link>
      </nav>

      <div className={styles.divider} />

      <div className={styles.myClubs}>
        <h3>My Clubs</h3>
        {userClubs.map((club) => (
          <div key={club.clubId} className={styles.clubItem}>
            <div className={styles.clubHeader} onClick={() => toggleClubMenu(club.clubId)}>
              
              <span className={styles.clubName}>{club.clubName}</span>
              {expandedClubs[club.clubId] ? <FaChevronDown className={styles.arrow} /> : <FaChevronRight className={styles.arrow} />}
            </div>
            {expandedClubs[club.clubId] && (
              <ul className={styles.clubLinks}>
                <li><Link to={`/clubs/${club.clubId}/details`}><HiOutlineClipboardList className={styles.subIcon} /> Details</Link></li>
                <li><Link to={`/clubs/${club.clubId}/announcements`}><HiOutlineSpeakerphone className={styles.subIcon} /> Announcements</Link></li>
                <li><Link to={`/clubs/${club.clubId}/events`}><BsCalendarEvent className={styles.subIcon} /> Events</Link></li>
                <li><Link to={`/clubs/${club.clubId}/qa`}><GoCommentDiscussion  className={styles.subIcon} /> Discussions</Link></li>
              </ul>
            )}
          </div>
        ))}
      </div>
      
      <button onClick={handleLogout} className={styles.logoutBtn}><span className={styles.icon}><RiLogoutCircleRLine /></span>Logout</button>
    </div>
  );
};

export default Sidebar;