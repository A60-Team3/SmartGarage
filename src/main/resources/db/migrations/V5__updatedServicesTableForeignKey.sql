ALTER TABLE services
    DROP CONSTRAINT fk_services_visits;

ALTER TABLE services
    ADD CONSTRAINT fk_services_visits
        FOREIGN KEY (visit_id) REFERENCES visits (id)
            ON DELETE CASCADE;