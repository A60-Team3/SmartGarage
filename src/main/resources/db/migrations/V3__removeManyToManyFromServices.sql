ALTER TABLE services
ADD service_type_id BIGINT NOT NULL;

ALTER TABLE services
ADD CONSTRAINT fk_services_service_types
    foreign key (service_type_id) references service_types(id);

DROP TABLE services_service_types