create table `school` (
    `name` varchar(255),
    `id` int(11) AUTO_INCREMENT PRIMARY KEY,
    `create_at` datetime,
    `update_at` datetime
);

create table `college` (
    `school_id` int(11),
    `name` varchar(255),
    `id` int(11) AUTO_INCREMENT PRIMARY KEY,
    `create_at` datetime,
    `update_at` datetime
);

create table `major` (
    `school_id` int(11),
    `college_id` int(11),
    `name` varchar(255),
    `id` int(11) AUTO_INCREMENT PRIMARY KEY,
    `create_at` datetime,
    `update_at` datetime
);

create table `user` (
    `username` varchar(255),
    `password` varchar(255),
    `role` varchar(255),
    `name` varchar(255),
    `sex` varchar(255),
    `school_id` int(11),
    `college_id` int(11),
    `major_id` int(11),
    `id` int(11) AUTO_INCREMENT PRIMARY KEY,
    `create_at` datetime,
    `update_at` datetime
);

create table `course` (
    `user_id` int(11),
    `name` varchar(255),
    `description` text,
    `chapter_count` int(11),
    `id` int(11) AUTO_INCREMENT PRIMARY KEY,
    `create_at` datetime,
    `update_at` datetime
);

create table `chappter` (
    `course_id` int(11),
    `title` varchar(255),
    `duration` int(11),
    `url` varchar(255),
    `problems` varchar(255),
    `id` int(11) AUTO_INCREMENT PRIMARY KEY,
    `create_at` datetime,
    `update_at` datetime
);

create table `user_course` (
    `user_id` int(11),
    `course_id` int(11),
    `id` int(11) AUTO_INCREMENT PRIMARY KEY,
    `create_at` datetime,
    `update_at` datetime
);

create table `exam` (
    `course_id` int(11),
    `user_id` int(11),
    `start_at` datetime,
    `end_at` datetime,
    `duration` int(11),
    `problems` varchar(255),
    `id` int(11) AUTO_INCREMENT PRIMARY KEY,
    `create_at` datetime,
    `update_at` datetime
);

create table `submission` (
    `user_id` int(11),
    `details` varchar(255),
    `score` int(11),
    `associate_id` int(11),
    `type` int(11),
    `id` int(11) AUTO_INCREMENT PRIMARY KEY,
    `create_at` datetime,
    `update_at` datetime
);

create table `discussion` (
    `course_id` int(11),
    `title` varchar(255),
    `content` text,
    `id` int(11) AUTO_INCREMENT PRIMARY KEY,
    `create_at` datetime,
    `update_at` datetime
);

create table `comment` (
    `discussion_id` int(11),
    `user_id` int(11),
    `content` text,
    `id` int(11) AUTO_INCREMENT PRIMARY KEY,
    `create_at` datetime,
    `update_at` datetime
);

create table `course_assistant` (
    `course_id` int(11),
    `user_id` int(11),
    `id` int(11) AUTO_INCREMENT PRIMARY KEY,
    `create_at` datetime,
    `update_at` datetime
);

create table `attachment` (
    `name` varchar(255),
    `url` varchar(255),
    `id` int(11) AUTO_INCREMENT PRIMARY KEY,
    `create_at` datetime,
    `update_at` datetime
);

create table `chapter_attachment` (
    `chapter_id` int(11),
    `attachment_id` int(11),
    `id` int(11) AUTO_INCREMENT PRIMARY KEY,
    `create_at` datetime,
    `update_at` datetime
);

create table `role` (
    `id` int(11) AUTO_INCREMENT PRIMARY KEY,
    `name` varchar(255),
    `description` varchar(255),
    `create_at` datetime,
    `update_at` datetime
);

create table `menu` (
    `id` int(11) AUTO_INCREMENT PRIMARY KEY,
    `parent_id` int(11),
    `name` varchar(255),
    `icon` varchar(255),
    `role` varchar(255),
    `href` varchar(255),
    `create_at` datetime,
    `update_at` datetime
);

create table `permission` (
    `id` int(11) AUTO_INCREMENT PRIMARY KEY,
    `role` varchar(255),
    `model` varchar(255),
    `operation` varchar(255),
    `create_at` datetime,
    `update_at` datetime
);



