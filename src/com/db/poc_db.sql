CREATE TABLE `user_details` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(100) NULL,
  `last_name` VARCHAR(100) NULL,
  `email_address` VARCHAR(200) NOT NULL,
  `telephone` VARCHAR(45) NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `email_address_UNIQUE` (`email_address` ASC));


CREATE TABLE `subscription_details` (
  `user_id` INT NOT NULL,
  `subscription_id` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`user_id`, `subscription_id`),
  UNIQUE INDEX `subscription_id_UNIQUE` (`subscription_id` ASC),
  CONSTRAINT `user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `hp_poc`.`user_details` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
ALTER TABLE `subscription_details` 
ADD COLUMN `subscription_name` VARCHAR(200) NULL AFTER `subscription_id`;
