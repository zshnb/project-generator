create table `role` (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255)
);

create table `menu` (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `parent_id` INT(11),
    `name` VARCHAR(255),
    `icon` VARCHAR(255),
    `role` VARCHAR(255),
    `href` VARCHAR(255)
);

create table `permission` (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `role` VARCHAR(255),
    `model` VARCHAR(255),
    `operation` VARCHAR(255)
);

create table `user` (
    `id` INT(11) AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(255),
    `password` VARCHAR(255),
    `role` VARCHAR(255)
);



