create table usuarios(
    id bigint not null auto_increment,
    login varchar(100) not null unique,
    senha varchar(255) not null,
    perfil varchar(10) not null,

    primary key(id)
);

-- senha em texto puro: 123456 (hash BCrypt)
insert into usuarios(login, senha, perfil)
values ('admin@autoescola.com', '$2a$10$0MzA13MUP59aqaavbmCGfO7Z1mf.wwPncXWvCXFDwYaGRuL4lIBOe', 'ADMIN');
