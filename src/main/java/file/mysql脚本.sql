-- 创建数据库
CREATE DATABASE Book_storage;

-- 使用创建的数据库
USE Book_storage;

-- 创建表单 `book`
CREATE TABLE book (
    title VARCHAR(255) NOT NULL,                 -- 书籍标题
    author VARCHAR(255) NOT NULL,                -- 书籍作者
    publicationDate INT,                         -- 出版日期
    ibsn VARCHAR(30) NOT NULL,                   -- ISBN
    bookType VARCHAR(100),                       -- 书籍类别
    quantity INT DEFAULT 0,                      -- 库存数量
    otherSize VARCHAR(1000),                     -- 其他信息
    PRIMARY KEY (ibsn)                           -- 将 ISBN 设置为主键
);

-- 创建表单 `user`
CREATE TABLE user (
    name VARCHAR(100) NOT NULL UNIQUE,           -- 用户名
    password VARCHAR(255) NOT NULL,              -- 密码
    userType VARCHAR(8) NOT NULL                -- 用户类型
);
