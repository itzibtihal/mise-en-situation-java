CREATE TABLE forests (
                         id VARCHAR(36) PRIMARY KEY,
                         name VARCHAR(255) NOT NULL
);

CREATE TABLE trees (
                       id VARCHAR(36) PRIMARY KEY,
                       type VARCHAR(255) NOT NULL,
                       forest_id VARCHAR(36),
                       FOREIGN KEY (forest_id) REFERENCES forests(id) ON DELETE CASCADE
);
