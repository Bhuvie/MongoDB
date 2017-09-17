CREATE TABLE `employee` (
  `Fname` varchar(45) DEFAULT NULL,
  `Minit` varchar(45) DEFAULT NULL,
  `Lname` varchar(45) DEFAULT NULL,
  `Ssn` varchar(45) NOT NULL,
  `Bdate` varchar(45) DEFAULT NULL,
  `Address` varchar(45) DEFAULT NULL,
  `Sex` varchar(45) DEFAULT NULL,
  `Salary` varchar(45) DEFAULT NULL,
  `Super_ssn` varchar(45) DEFAULT NULL,
  `Dno` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Ssn`)
) ;


CREATE TABLE `department` (
  `Dname` varchar(45) DEFAULT NULL,
  `Dnumber` varchar(45) NOT NULL,
  `Mgr_ssn` varchar(45) DEFAULT NULL,
  `Mgr_start_date` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Dnumber`)
) ;

CREATE TABLE `dept_locations` (
  `Dnumber` VARCHAR(45) NOT NULL,
  `Dlocations` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Dlocations`, `Dnumber`),
  CONSTRAINT `Dnumber`
    FOREIGN KEY (`Dnumber`)
    REFERENCES `bhuviedb`.`department` (`Dnumber`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
    CREATE TABLE `project` (
  `Pname` VARCHAR(45) NULL,
  `Pnumber` VARCHAR(45) NOT NULL,
  `Plocation` VARCHAR(45) NULL,
  `Dnum` VARCHAR(45) NULL,
  PRIMARY KEY (`Pnumber`),
  INDEX `Dnum_idx` (`Dnum` ASC),
  CONSTRAINT `Dnum`
    FOREIGN KEY (`Dnum`)
    REFERENCES `bhuviedb`.`department` (`Dnumber`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
    CREATE TABLE `works_on` (
  `Essn` VARCHAR(45) NOT NULL,
  `Pno` VARCHAR(45) NOT NULL,
  `Hours` VARCHAR(45) NULL,
  PRIMARY KEY (`Essn`, `Pno`),
  INDEX `Pno_idx` (`Pno` ASC),
  CONSTRAINT `Essn`
    FOREIGN KEY (`Essn`)
    REFERENCES `bhuviedb`.`employee` (`Ssn`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `Pno`
    FOREIGN KEY (`Pno`)
    REFERENCES `bhuviedb`.`project` (`Pnumber`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



