CREATE TABLE Users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('USER', 'ADMIN'))
);

CREATE TABLE Currencies (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    code VARCHAR(3) UNIQUE NOT NULL,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE Categories (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE Offers (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    amount DECIMAL(15,2) NOT NULL,
    user_id INTEGER NOT NULL,
    currency_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (currency_id) REFERENCES Currencies(id),
    FOREIGN KEY (category_id) REFERENCES Categories(id)
);

CREATE TABLE Notifications (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    message TEXT NOT NULL,
    user_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE AuditLogs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    action VARCHAR(50) NOT NULL,
    details TEXT,
    user_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

INSERT INTO Currencies (code, name) VALUES ('USD', 'US Dollar'), ('EUR', 'Euro');
INSERT INTO Categories (name) VALUES ('ИТ-оборудование'), ('Программное обеспечение');
INSERT INTO Users (username, password, role) VALUES ('admin1', '$2a$10$exampleHash', 'ADMIN');