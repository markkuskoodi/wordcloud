--Table which stores metadata about the uploaded text file
CREATE TABLE TextFile(
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP NOT NULL
);

--Table which keeps track of the text processing
CREATE TABLE TextProcessProgress(
    process_id SERIAL PRIMARY KEY NOT NULL,
    capsuled_message_count BIGINT NOT NULL,
    currently_processed BIGINT NOT NULL DEFAULT 0,
    text_file_id VARCHAR(255) NOT NULL,
    CONSTRAINT fk_TextFile FOREIGN KEY (text_file_id) REFERENCES TextFile(id) ON DELETE CASCADE
);

--Table, which temporarily holds all the words from the uploaded text files. It will be cleared after
--the results of the text processing (word, word count, text_file_id) is inserted into "textresult" table.
CREATE TABLE TextProcess(
    id SERIAL PRIMARY KEY NOT NULL,
    word varchar(255) NOT NULL,
    text_file_id VARCHAR(255) NOT NULL,
    CONSTRAINT fk_TextFile FOREIGN KEY (text_file_id) REFERENCES TextFile(id)
);

--Table, which stores result of the text processing.
CREATE TABLE TextResult(
    id SERIAL PRIMARY KEY,
    text_file_id VARCHAR(255) NOT NULL,
    word VARCHAR(255) NOT NULL,
    count BIGINT NOT NULL,
    CONSTRAINT fk_TextFile FOREIGN KEY (text_file_id) REFERENCES TextFile(id) ON DELETE CASCADE
);

--A crontab job which deletes all the rows in the textfile, textresult and textprocessprogress tables which are older
--than two hours.
SELECT cron.schedule('0 */2 * * *', $$DELETE FROM textfile WHERE creation_date < now()$$);







