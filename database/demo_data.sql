-- Demo data for AI Tour Planner (all people/companies are fictional).
-- Load AFTER schema.sql:  mysql -u root -p tour_planner < database/demo_data.sql
-- Logins (username / password):  admin/admin · provider: greentrails/demo123 · guide: arjun/demo123 · traveller: meera/demo123
USE `tour_planner`;
SET FOREIGN_KEY_CHECKS=0;
TRUNCATE `login`; TRUNCATE `place_category`; TRUNCATE `places`; TRUNCATE `tour_provider`; TRUNCATE `packages`;
TRUNCATE `customer`; TRUNCATE `booking`; TRUNCATE `payment`; TRUNCATE `review_ratings`; TRUNCATE `enquiry`;
TRUNCATE `complaint`; TRUNCATE `favorites`; TRUNCATE `chat`; TRUNCATE `chatai`; TRUNCATE `guid`; TRUNCATE `markedlocation`;
SET FOREIGN_KEY_CHECKS=1;

INSERT INTO `login` VALUES
(1,'admin','admin','admin'),
(2,'greentrails','demo123','provider'),
(3,'backwaterbliss','demo123','provider'),
(4,'coastalcompass','demo123','pending'),
(5,'meera','demo123','customer'),
(6,'daniel','demo123','customer'),
(7,'priya','demo123','customer'),
(8,'arjun','demo123','guid'),
(9,'lakshmi','demo123','pending');

INSERT INTO `place_category` VALUES (1,'Hill Station'),(2,'Backwaters'),(3,'Beach'),(4,'Heritage'),(5,'Wildlife & Trekking');

INSERT INTO `places` VALUES
(1,1,'Munnar','Rolling tea estates, misty peaks and Eravikulam National Park','static/demo/munnar.jpg','10.0889','77.0595'),
(2,2,'Alleppey Backwaters','Overnight houseboat cruises through palm-lined canals','static/demo/alleppey.jpg','9.4981','76.3388'),
(3,3,'Varkala Cliff','Red laterite cliffs above a quiet Arabian Sea beach','static/demo/varkala.jpg','8.7379','76.7163'),
(4,3,'Kollam Beach','Wide sunset beach next to the historic port town','static/demo/kollam.jpg','8.8768','76.5926'),
(5,4,'Fort Kochi','Chinese fishing nets, colonial streets and spice markets','static/demo/kochi.jpg','9.9658','76.2421'),
(6,5,'Wayanad','Chembra Peak trek, waterfalls and spice plantations','static/demo/wayanad.jpg','11.6854','76.1320');

INSERT INTO `tour_provider` VALUES
(1,'Green Trails Kerala','Munnar','Idukki','685612','+91 90000 00001','hello@greentrails.example.com',2),
(2,'Backwater Bliss Tours','Alappuzha','Alappuzha','688001','+91 90000 00002','book@backwaterbliss.example.com',3),
(3,'Coastal Compass','Varkala','Thiruvananthapuram','695141','+91 90000 00003','team@coastalcompass.example.com',4);

INSERT INTO `packages` VALUES
(1,1,'Munnar Tea Country Escape','1','8500','3','2','2','0','Public'),
(2,2,'Alleppey Houseboat Overnight','2','12000','2','1','2','1','Public'),
(3,1,'Wayanad Trek & Waterfalls','6','9800','3','2','2','0','Public'),
(4,2,'Fort Kochi Heritage Walk','5','3500','1','0','2','2','Public'),
(5,3,'Varkala Cliff & Beach Retreat','3','7200','3','2','2','0','pending'),
(6,3,'Kollam Sunset & Ashtamudi Lake','4','5400','2','1','2','1','pending');

INSERT INTO `customer` VALUES
(1,'Meera','Nair','Rose Villa','Kottayam','Kottayam','India','686001','+91 90000 00011','meera@example.com','','female','1998-04-12',5,'9.5916','76.5222'),
(2,'Daniel','Walsh','12 Harbour Rd','Dublin','Dublin','Ireland','D02','+353 80 000 0012','daniel@example.com','X0000000','male','1994-09-03',6,'53.3498','-6.2603'),
(3,'Priya','Menon','Sree Nilayam','Kochi','Ernakulam','India','682001','+91 90000 00013','priya@example.com','','female','2000-01-25',7,'9.9312','76.2673');

INSERT INTO `booking` VALUES
(1,1,1,'2','2026-08-02','14/08/2026','17000','confirmed'),
(2,2,2,'3','2026-08-10','22/08/2026','36000','confirmed'),
(3,4,3,'4','2026-09-01','12/09/2026','14000','pending'),
(4,3,2,'2','2026-09-05','03/10/2026','19600','pending');

INSERT INTO `payment` VALUES (1,1,'17000','UPI','2026-08-02 10:14'),(2,2,'36000','Card','2026-08-10 18:40');

INSERT INTO `review_ratings` VALUES
(1,1,1,'5','Tea estate walk at sunrise was unforgettable','2026-08-18'),
(2,2,2,'4','Lovely houseboat and food, a bit hot at noon','2026-08-24'),
(3,4,3,'5','Great guide, learned so much about Kochi','2026-09-13');

INSERT INTO `enquiry` VALUES
(1,2,1,'Is the Wayanad trek suitable for beginners?','Yes, the pace is easy and a guide joins you.','2026-09-04'),
(2,3,2,'Can we add a Kathakali show in Kochi?','pending','2026-09-10');

INSERT INTO `complaint` VALUES (1,2,'Pickup was 30 minutes late on day one','Sorry! We have spoken to the driver.','2026-08-23');

INSERT INTO `guid` VALUES
(1,8,1,'Arjun','Pillai','Munnar','+91 90000 00021','arjun@example.com','10.0889','77.0595'),
(2,9,5,'Lakshmi','Varma','Fort Kochi','+91 90000 00022','lakshmi@example.com','9.9658','76.2421');

INSERT INTO `markedlocation` VALUES
(1,1,'10.1504','77.0599','Top Station viewpoint, best at sunrise',1),
(2,1,'10.0927','77.0582','Local tea factory tour & tasting',1);
