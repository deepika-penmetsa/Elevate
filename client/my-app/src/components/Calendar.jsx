import { useState } from "react";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import styles from "../styles/Calendar.module.css";

const eventData = [
  { id: 2, title: "Event 1", date: "2025-03-04", description: "Content for event 1." },
  { id: 10, title: "Event 5", date: "2025-03-10", description: "Content for event 5." },
  { id: 15, title: "Music Workshop", date: "2025-03-25", description: "A workshop on music theory and practice." },
  { id: 20, title: "Chess Tournament", date: "2025-03-22", description: "Annual chess tournament for all skill levels." },
  { id: 25, title: "Art Exhibition", date: "2025-03-30", description: "A showcase of artwork from club members." }
];

const CustomCalendar = () => {
  const [selectedDate, setSelectedDate] = useState(null);
  const [expanded, setExpanded] = useState(false);
  
  const toggleCalendar = () => setExpanded(!expanded);

  // Find the 3 upcoming events
  const upcomingEvents = eventData
    .filter(event => new Date(event.date) >= new Date())
    .sort((a, b) => new Date(a.date) - new Date(b.date))
    .slice(0, 1);

  // Get events for the selected date
  const selectedEvents = selectedDate
    ? eventData.filter(event => event.date === selectedDate.toISOString().split("T")[0])
    : [];

  return (
    <div className={styles.calendarContainer}>
      {/* Calendar Header - Click to Expand */}
      <div className={styles.calendarHeader} onClick={toggleCalendar}>
        <h3>📅 Calendar</h3>
        <span className={styles.toggleIcon}>{expanded ? "▲" : "▼"}</span>
      </div>

      {expanded && (
        <div className={styles.fullCalendar}>
          <Calendar
            onChange={setSelectedDate}
            value={selectedDate}
            tileContent={({ date }) => {
              const formattedDate = date.toISOString().split("T")[0];
              const hasEvent = eventData.some(event => event.date === formattedDate);

              return hasEvent ? <div className="eventDot"></div> : null;
            }}
          />
        </div>
      )}

      {/* Display Upcoming Events When Calendar is Closed */}
      {!expanded && upcomingEvents.length > 0 && (
        <div className={styles.upcomingEvents}>
          {upcomingEvents.map(event => (
            <div key={event.id} className={styles.eventItem}>
              <span className={styles.eventDot}></span>
              <p>
                <strong>{event.date.split("-").reverse().join(" ")}</strong> - {event.title}
              </p>
            </div>
          ))}
        </div>
      )}

      {/* Selected Date Events */}
      {expanded && selectedDate && (
        <div className={styles.eventList}>
          {selectedEvents.length > 0 ? (
            selectedEvents.map(event => (
              <div key={event.id} className={styles.eventItem}>
                <span className={styles.eventDot}></span>
                <p>
                  <strong>{event.date.split("-").reverse().join(" ")}</strong> - {event.title}
                </p>
              </div>
            ))
          ) : (
            <p className={styles.noEvents}>No events on this day.</p>
          )}
        </div>
      )}
    </div>
  );
};

export default CustomCalendar;