-- Create patient table
CREATE TABLE IF NOT EXISTS `patient` (
                                         `id` int NOT NULL AUTO_INCREMENT,
                                         `name` varchar(255) DEFAULT NULL,
    `address` varchar(255) DEFAULT NULL,
    `phone` varchar(20) DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create treatment table
CREATE TABLE IF NOT EXISTS `treatment` (
                                           `id` int NOT NULL AUTO_INCREMENT,
                                           `type` varchar(50) DEFAULT NULL,
    `price` decimal(10,2) DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create appointment table
CREATE TABLE IF NOT EXISTS `appointment` (
                                             `id` int NOT NULL AUTO_INCREMENT,
                                             `patient_id` int DEFAULT NULL,
                                             `channel_date` date DEFAULT NULL,
                                             `channel_time` time DEFAULT NULL,
                                             `reg_fee_status` tinyint(1) DEFAULT NULL,
    `treatment_id` int DEFAULT NULL,
    `appointment_status` varchar(20) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `patient_id` (`patient_id`),
    KEY `treatment_id` (`treatment_id`),
    CONSTRAINT `appointment_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`),
    CONSTRAINT `appointment_ibfk_2` FOREIGN KEY (`treatment_id`) REFERENCES `treatment` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create payment table
CREATE TABLE IF NOT EXISTS `payment` (
                                         `id` int NOT NULL AUTO_INCREMENT,
                                         `appointment_id` int DEFAULT NULL,
                                         `total_fee` decimal(10,2) DEFAULT NULL,
    `payment_date` date DEFAULT NULL,
    `payment_status` varchar(20) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `appointment_id` (`appointment_id`),
    CONSTRAINT `payment_ibfk_1` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
