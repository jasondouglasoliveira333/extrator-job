insert into corporation(id, name, root_cnpj, retroative_days) values(1001, 'jason', '36352242000191', 30);
insert into mail_configuration(id, mail_configuration_type, corporation_id) values(1, 'OFFICE_365', 1001);
insert into mail_info (id,host, port, protocol, username, password, mail_configuration_id) values(1000, 'imap.gmail.com', 993, 'IMAP', 'jasondouglasoliveira33333@gmail.com', 'poedflsjkuypucmw', 1);
insert into sap_configuration(id, destination_name, entry_point, host, username, passwd, sysnr, client, lang, directory, corporation_id) values (1, 'RFC_BTP_H19', '/LKMT/COM_JCO_SAP', '192.168.197.24', 'integracao', '123456', '00', '100', 'PT', null, 1001);
insert into parameter(id, name, value) values (1, 'SAP_FALLBACK', 'false');  


