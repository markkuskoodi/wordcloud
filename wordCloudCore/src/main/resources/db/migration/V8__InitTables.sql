CREATE TABLE TextFile(
    id VARCHAR(108) NOT NULL PRIMARY KEY,
    file_name VARCHAR(100) NOT NULL
);

CREATE TABLE TextProcess(
    id SERIAL NOT NULL PRIMARY KEY,
    word varchar(255) NOT NULL,
    text_file_id VARCHAR(108) NOT NULL,
    possible_typo BOOLEAN NOT NULL DEFAULT false,
    CONSTRAINT fk_TextFile FOREIGN KEY (text_file_id) REFERENCES TextFile(id)
);



