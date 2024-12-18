-- create the database
DROP DATABASE IF EXISTS hotel_management;
CREATE DATABASE hotel_management;

-- select the database
USE hotel_management;

-- Create Tables

CREATE TABLE Guests (
    guest_id INT PRIMARY KEY,
    name VARCHAR(50),
    contact_info VARCHAR(100),
    id_number VARCHAR(20) UNIQUE
);

CREATE TABLE Rooms (
    room_id INT PRIMARY KEY,
    room_type VARCHAR(20),
    capacity INT,
    amenities VARCHAR(255),
    price DECIMAL(10, 2),
    status VARCHAR(20)
);

CREATE TABLE Reservations (
    reservation_id INT PRIMARY KEY,
    guest_id INT,
    room_id INT,
    check_in_date DATE,
    check_out_date DATE,
    status VARCHAR(20),
    FOREIGN KEY (guest_id) REFERENCES Guests(guest_id),
    FOREIGN KEY (room_id) REFERENCES Rooms(room_id)
);

CREATE TABLE Payments (
    payment_id INT PRIMARY KEY,
    reservation_id INT,
    amount DECIMAL(10, 2),
    payment_date DATE,
    payment_method VARCHAR(50),
    FOREIGN KEY (reservation_id) REFERENCES Reservations(reservation_id)
);

CREATE TABLE Staff (
    staff_id INT PRIMARY KEY,
    name VARCHAR(50),
    role VARCHAR(50),
    shift VARCHAR(20),
    contact_info VARCHAR(100)
);

CREATE TABLE Services (
    service_id INT PRIMARY KEY,
    service_name VARCHAR(50),
    cost DECIMAL(10, 2),
    availability VARCHAR(50)
);

CREATE TABLE ServiceRequests (
    service_request_id INT PRIMARY KEY,
    guest_id INT,
    service_id INT,
    request_date DATE,
    status VARCHAR(20),
    FOREIGN KEY (guest_id) REFERENCES Guests(guest_id),
    FOREIGN KEY (service_id) REFERENCES Services(service_id)
);

CREATE TABLE MaintenanceLogs (
    log_id INT PRIMARY KEY,
    room_id INT,
    staff_id INT,
    description VARCHAR(255),
    date DATE,
    FOREIGN KEY (room_id) REFERENCES Rooms(room_id),
    FOREIGN KEY (staff_id) REFERENCES Staff(staff_id)
);

-- Populate the tables

INSERT INTO Guests (guest_id, name, contact_info, id_number)
VALUES 
	(1, 'John', 'john@gmail.com', 'ID101'),
	(2, 'Jane', 'jane@gmail.com', 'ID102'),
	(3, 'Alice', 'alice@gmail.com', 'ID103'),
	(4, 'Bob', 'bob@gmail.com', 'ID104'),
	(5, 'Carol', 'carol@gmail.com', 'ID105'),
	(6, 'Daniel', 'daniel@gmail.com', 'ID106'),
	(7, 'Emma', 'emma@gmail.com', 'ID107'),
	(8, 'Fiona', 'fiona@gmail.com', 'ID108'),
	(9, 'George', 'george@gmail.com', 'ID109'),
	(10, 'Hannah', 'hannah@gmail.com', 'ID1010'),
	(11, 'Isaac', 'isaac@gmail.com', 'ID1011'),
	(12, 'Jack', 'jack@gmail.com', 'ID112'),
	(13, 'Karen', 'karen@gmail.com', 'ID113'),
	(14, 'Liam', 'liam@gmail.com', 'ID114'),
	(15, 'Mona', 'mona@gmail.com', 'ID115');

INSERT INTO Rooms (room_id, room_type, capacity, amenities, price, status)
VALUES 
	(101, 'Single', 1, 'WiFi, TV', 100.00, 'Available'),
	(102, 'Double', 2, 'WiFi, TV, MiniBar', 150.00, 'Occupied'),
	(103, 'Suite', 4, 'WiFi, TV, MiniBar, Kitchen', 300.00, 'Available'),
	(104, 'Single', 1, 'WiFi, TV', 100.00, 'Reserved'),
	(105, 'Double', 2, 'WiFi, TV, MiniBar', 150.00, 'Occupied'),
	(106, 'Suite', 4, 'WiFi, TV, MiniBar, Kitchen', 300.00, 'Available'),
	(107, 'Single', 1, 'WiFi, TV', 100.00, 'Reserved'),
	(108, 'Double', 2, 'WiFi, TV, MiniBar', 150.00, 'Occupied'),
	(109, 'Suite', 4, 'WiFi, TV, MiniBar, Kitchen', 300.00, 'Available'),
	(110, 'Single', 1, 'WiFi, TV', 100.00, 'Reserved'),
	(111, 'Double', 2, 'WiFi, TV, MiniBar', 150.00, 'Occupied'),
	(112, 'Suite', 4, 'WiFi, TV, MiniBar, Kitchen', 300.00, 'Available'),
	(113, 'Single', 1, 'WiFi, TV', 100.00, 'Reserved'),
	(114, 'Double', 2, 'WiFi, TV, MiniBar', 150.00, 'Occupied'),
	(115, 'Suite', 4, 'WiFi, TV, MiniBar, Kitchen', 300.00, 'Available');

INSERT INTO Reservations (reservation_id, guest_id, room_id, check_in_date, check_out_date, status)
VALUES 
    (1, 1, 101, '2024-11-01', '2024-11-05', 'Completed'),
    (2, 2, 102, '2024-11-02', '2024-11-07', 'Completed'),
    (3, 3, 103, '2024-11-03', '2024-11-10', 'Completed'),
    (4, 4, 104, '2024-11-04', '2024-11-06', 'Checked-In'),
    (5, 5, 105, '2024-11-05', '2024-11-09', 'Reserved'),
    (6, 6, 106, '2024-11-06', '2024-11-12', 'Completed'),
    (7, 7, 107, '2024-11-07', '2024-11-09', 'Completed'),
    (8, 8, 108, '2024-11-08', '2024-11-10', 'Checked-In'),
    (9, 9, 109, '2024-11-09', '2024-11-14', 'Completed'),
    (10, 10, 110, '2024-11-10', '2024-11-12', 'Completed'),
    (11, 11, 111, '2024-11-11', '2024-11-15', 'Completed'),
    (12, 12, 112, '2024-11-12', '2024-11-20', 'Reserved'),
    (13, 13, 113, '2024-11-13', '2024-11-14', 'Completed'),
    (14, 14, 114, '2024-11-14', '2024-11-18', 'Checked-In'),
    (15, 15, 115, '2024-11-15', '2024-11-19', 'Completed'),
    (16, 1, 101, '2024-11-16', '2024-11-20', 'Completed'),
    (17, 2, 102, '2024-11-17', '2024-11-18', 'Completed'),
    (18, 3, 103, '2024-11-18', '2024-11-22', 'Completed'),
    (19, 3, 104, '2024-11-19', '2024-11-24', 'Completed'),
    (20, 4, 105, '2024-11-20', '2024-11-22', 'Completed'),
    (21, 5, 106, '2024-11-21', '2024-11-25', 'Completed'),
    (22, 6, 107, '2024-11-22', '2024-11-26', 'Completed');
    
INSERT INTO Payments (payment_id, reservation_id, amount, payment_date, payment_method)
VALUES 
    (1, 1, 400.00, '2024-11-05', 'Credit Card'),
    (2, 2, 150.00, '2024-11-06', 'Cash'),
    (3, 3, 300.00, '2024-11-07', 'Credit Card'),
    (4, 4, 100.00, '2024-11-08', 'Credit Card'),
    (5, 5, 200.00, '2024-11-09', 'Cash'),
    (6, 6, 450.00, '2024-11-10', 'Credit Card'),
    (7, 7, 250.00, '2024-11-11', 'Credit Card'),
    (8, 8, 150.00, '2024-11-12', 'Cash'),
    (9, 9, 500.00, '2024-11-13', 'Credit Card'),
    (10, 10, 150.00, '2024-11-14', 'Cash'),
    (11, 11, 300.00, '2024-11-15', 'Credit Card'),
    (12, 12, 350.00, '2024-11-16', 'Credit Card'),
    (13, 13, 100.00, '2024-11-17', 'Cash'),
    (14, 14, 300.00, '2024-11-18', 'Credit Card'),
    (15, 15, 400.00, '2024-11-19', 'Cash');
    
INSERT INTO Staff (staff_id, name, role, shift, contact_info)
VALUES 
	(1, 'Michael', 'Manager', 'Morning', 'michael@hotel.com'),
	(2, 'Sara', 'Receptionist', 'Evening', 'sara@hotel.com'),
	(3, 'Tom', 'Housekeeping', 'Day', 'tom@hotel.com'),
	(4, 'Anna', 'Maintenance', 'Night', 'anna@hotel.com'),
	(5, 'Lucas', 'Security', 'Day', 'lucas@hotel.com'),
	(6, 'Ella', 'Manager', 'Morning', 'ella@hotel.com'),
	(7, 'Charlie', 'Receptionist', 'Evening', 'charlie@hotel.com'),
	(8, 'Sophia', 'Housekeeping', 'Day', 'sophia@hotel.com'),
	(9, 'David', 'Maintenance', 'Night', 'david@hotel.com'),
	(10, 'Olivia', 'Security', 'Day', 'olivia@hotel.com'),
	(11, 'Peter', 'Manager', 'Morning', 'peter@hotel.com'),
	(12, 'Laura', 'Receptionist', 'Evening', 'laura@hotel.com'),
	(13, 'Henry', 'Housekeeping', 'Day', 'henry@hotel.com'),
	(14, 'Linda', 'Maintenance', 'Night', 'linda@hotel.com'),
	(15, 'Ryan', 'Security', 'Day', 'ryan@hotel.com');

INSERT INTO Services (service_id, service_name, cost, availability)
VALUES 
	(1, 'Room Service', 20.00, '24/7'),
	(2, 'Spa', 50.00, '9 AM - 9 PM'),
	(3, 'Laundry', 15.00, '24/7'),
	(4, 'Gym', 10.00, '6 AM - 11 PM'),
	(5, 'Concierge', 30.00, '24/7'),
	(6, 'Car Rental', 40.00, '8 AM - 8 PM'),
	(7, 'Tour Desk', 25.00, '10 AM - 6 PM'),
	(8, 'Massage', 60.00, '9 AM - 5 PM'),
	(9, 'WiFi', 5.00, '24/7'),
	(10, 'Parking', 10.00, '24/7'),
	(11, 'Shuttle', 35.00, '24/7'),
	(12, 'Child Care', 45.00, '7 AM - 8 PM'),
	(13, 'Conference Room', 150.00, '24/7'),
	(14, 'Pet Sitting', 70.00, '8 AM - 6 PM'),
	(15, 'Fitness Trainer', 80.00, '7 AM - 10 PM');
    
INSERT INTO ServiceRequests (service_request_id, guest_id, service_id, request_date, status)
VALUES 
    (1, 1, 1, '2024-11-01', 'Completed'),
    (2, 2, 2, '2024-11-02', 'Completed'),
    (3, 3, 3, '2024-11-03', 'Completed'),
    (4, 4, 4, '2024-11-04', 'Completed'),
    (5, 5, 1, '2024-11-05', 'Completed'),
    (6, 6, 5, '2024-11-06', 'Completed'),
    (7, 7, 6, '2024-11-07', 'Completed'),
    (8, 3, 2, '2024-11-08', 'Completed'),
    (9, 3, 7, '2024-11-09', 'Completed'),
    (10, 9, 3, '2024-11-10', 'Completed'),
    (11, 11, 8, '2024-11-11', 'Completed'),
    (12, 6, 4, '2024-11-12', 'Completed'),
    (13, 6, 5, '2024-11-13', 'Completed'),
    (14, 14, 6, '2024-11-14', 'Completed'),
    (15, 15, 7, '2024-11-15', 'Completed');

    
INSERT INTO MaintenanceLogs (log_id, room_id, staff_id, description, date)
VALUES 
    (1, 101, 4, 'Faucet repair', '2024-11-01'),
    (2, 102, 5, 'Air conditioning fix', '2024-11-02'),
    (3, 103, 4, 'Painting', '2024-11-03'),
    (4, 104, 3, 'Carpet cleaning', '2024-11-04'),
    (5, 105, 4, 'Window repair', '2024-11-05'),
    (6, 106, 5, 'Door lock replacement', '2024-11-06'),
    (7, 107, 3, 'AC repair', '2024-11-07'),
    (8, 108, 5, 'Bathroom plumbing', '2024-11-08'),
    (9, 109, 4, 'TV repair', '2024-11-09'),
    (10, 110, 3, 'Fridge replacement', '2024-11-10'),
    (11, 111, 5, 'Furniture fix', '2024-11-11'),
    (12, 112, 4, 'Electrical maintenance', '2024-11-12'),
    (13, 113, 5, 'Curtain replacement', '2024-11-13'),
    (14, 114, 3, 'Showerhead repair', '2024-11-14'),
    (15, 115, 4, 'WiFi setup', '2024-11-15');
    
-- Queries:

-- What is the average length of stay for each room type, and how many services were requested on average per 
-- reservation for those room types?

select
	Rooms.room_type, 
    round(avg(date(Reservations.check_out_date) - date(Reservations.check_in_date)), 2) as avg_length_of_stay,
    round(avg(service_request_count), 2) as avg_service_requests_per_reservation
from rooms
join Reservations on (Rooms.room_id = Reservations.room_id)
left join
	(
        select
            Reservations.reservation_id,
            count(ServiceRequests.service_request_id) as service_request_count
        from Reservations 
        left join
            ServiceRequests on Reservations.guest_id = ServiceRequests.guest_id
        group by Reservations.reservation_id
    ) as ServiceCounts on Reservations.reservation_id = ServiceCounts.reservation_id
group by Rooms.room_type;

-- How much revenue has each type of room generated?

select
    Rooms.room_type,
    sum(Payments.amount) as total_revenue
from Rooms
join Reservations on (Rooms.room_id = Reservations.room_id)
join Payments on (Reservations.reservation_id = Payments.reservation_id)
where Reservations.status = 'Completed'
group by Rooms.room_type;

-- Which services were most frequently requested by guests who stayed in a suite, and how many times was each service requested?

SELECT 
    s.service_name, 
    COUNT(sr.service_request_id) AS request_count
FROM 
    ServiceRequests sr
JOIN 
    Reservations r ON sr.guest_id = r.guest_id
JOIN 
    Rooms ro ON r.room_id = ro.room_id
JOIN 
    Services s ON sr.service_id = s.service_id
WHERE 
    ro.room_type = 'Suite'
GROUP BY 
    s.service_name
ORDER BY 
    request_count DESC;


-- Which staff members have completed the most maintenance tasks, and which rooms have they serviced the most?

select
    staff.name as staff_member,
    count(logs.log_id) as tasks_completed,
    (
        select room_id
        from MaintenanceLogs
        where staff_id = logs.staff_id
        group by room_id
        order by count(*) desc
        limit 1
    ) as most_serviced_room,
    (
        select count(*)
        from MaintenanceLogs
        where staff_id = logs.staff_id
        and room_id = (
            select room_id
            from MaintenanceLogs
            where staff_id = logs.staff_id
            group by room_id
            order by count(*) desc
            limit 1
        )
    ) as room_service_count
from MaintenanceLogs logs
join Staff staff on (logs.staff_id = staff.staff_id)
group by staff.name, logs.staff_id
order by tasks_completed desc, room_service_count desc;

-- Which room types have been reserved the most?

SELECT 
    Rooms.room_type, 
    COUNT(Reservations.reservation_id) AS total_reservations
FROM 
    Rooms
JOIN 
    Reservations ON Rooms.room_id = Reservations.room_id
GROUP BY 
    Rooms.room_type
ORDER BY 
    total_reservations DESC;
    
    
-- Which guests have paid the most for their reservations in total, including any services (exlcude null and 0)?

SELECT 
    Guests.guest_id, 
    Guests.name, 
    SUM(Payments.amount) + SUM(Services.cost) AS total_spent
FROM 
    Guests
JOIN 
    Reservations ON Guests.guest_id = Reservations.guest_id
JOIN 
    Payments ON Reservations.reservation_id = Payments.reservation_id
LEFT JOIN 
    ServiceRequests ON Guests.guest_id = ServiceRequests.guest_id
LEFT JOIN 
    Services ON ServiceRequests.service_id = Services.service_id
WHERE 
    Payments.amount IS NOT NULL AND Services.cost IS NOT NULL
GROUP BY 
    Guests.guest_id, Guests.name
ORDER BY 
    total_spent DESC;
    
-- Trigger: How can we ensure that a room's status updates to 'Occupied' when a guest checks in?

drop trigger if exists  room_status_update;

DELIMITER //

create trigger room_status_update
	after update on rooms
    for each row
begin
	-- when they check in, change the room to occupied
	if new.status = 'Checked In' then
		update rooms
		set status = 'Occupied'
		where room_id = new.room_id;
	end if;
    
    -- when they check out, change the room status to available
    if new.status = 'Available' then
		update rooms
        set status = 'Available'
        where room_id = new.room_id;
	end if;
    
end //

delimiter ;
    
-- testing trigger
update rooms
set status  = 'Occupied'
where room_id = 109;

select * from rooms;
