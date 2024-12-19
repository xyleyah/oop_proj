USE easdata;

-- Drop the table first (only run this once)
DROP TABLE IF EXISTS events;

-- Then create the events table
CREATE TABLE events (
    id INT AUTO_INCREMENT PRIMARY KEY,
    faculty VARCHAR(255) NOT NULL,
    event_name VARCHAR(255) NOT NULL,
    event_date DATE NOT NULL,
    event_details TEXT NOT NULL,
    status VARCHAR(255) NOT NULL,    -- Make sure this matches your Java code
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

SELECT * FROM events;

-- Create attendance table
CREATE TABLE IF NOT EXISTS attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(50) NOT NULL,
    faculty VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_student_date (student_id, faculty)
);

-- View the attendance table
SELECT * FROM attendance;

-- View events for a specific faculty
SELECT * FROM events WHERE faculty = 'your_faculty';

-- View events on a specific date
SELECT * FROM events WHERE event_date = 'YYYY-MM-DD';

-- Delete an event (be careful with this!)
DELETE FROM events WHERE faculty = 'your_faculty';

-- View attendance for a specific faculty
SELECT * FROM attendance WHERE faculty = 'FACET';

-- View attendance for a specific student
SELECT * FROM attendance WHERE student_id = '123456';

-- Update event status
UPDATE events SET event_status = 'APPROVED' WHERE id = your_event_id;

-- Count events by status
SELECT status, COUNT(*) as Count 
FROM events 
GROUP BY status;
