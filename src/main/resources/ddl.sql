create table items (
                       id binary(16) not null,
                       created_on datetime(6) not null,
                       updated_on datetime(6),
                       available_stock_units bigint,
                       description varchar(255),
                       name varchar(255),
                       price decimal(38,2),
                       weight integer,
                       primary key (id)
) engine=InnoDB;

create table orders (
                        id binary(16) not null,
                        created_on datetime(6) not null,
                        updated_on datetime(6),
                        address_line1 varchar(255) not null,
                        address_line2 varchar(255),
                        amount decimal(38,2),
                        city varchar(255) not null,
                        email varchar(255),
                        phone_number varchar(255) not null,
                        pin_code varchar(255) not null,
                        placed_on datetime(6) not null,
                        state varchar(255) not null,
                        status enum ('PLACED','PROCESSING','SHIPPED','DELIVERED','CANCELLED','REFUNDED'),
                        primary key (id)
) engine=InnoDB;

create table orders_items_association (
                                          id binary(16) not null,
                                          item_id binary(16),
                                          order_id binary(16),
                                          quantity integer,
                                          primary key (id)
) engine=InnoDB;

create table orders_order_items (
                                    order_entity_id binary(16) not null,
                                    order_items_id binary(16) not null,
                                    primary key (order_entity_id, order_items_id)
) engine=InnoDB;

alter table orders_order_items
drop index UK_9d47gapmi35omtannusv6btu3;

alter table orders_order_items
    add constraint UK_9d47gapmi35omtannusv6btu3 unique (order_items_id);

alter table orders_order_items
    add constraint FKafk2kv447ev0ersp2c9tp2q5g
        foreign key (order_items_id)
            references orders_items_association (id);

alter table orders_order_items
    add constraint FKbo9ac5al9205yib4tmu0mnshh
        foreign key (order_entity_id)
            references orders (id);