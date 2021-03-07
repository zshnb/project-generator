create table project
(
    name varchar(255),
    json json,
    description text,
    `id` int(11) AUTO_INCREMENT PRIMARY KEY,
    `create_at` datetime,
    `update_at` datetime
)