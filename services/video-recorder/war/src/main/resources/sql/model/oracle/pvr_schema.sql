--
-- Copyright 2014 Comcast Cable Communications Management, LLC
--
-- This file is part of CATS.
--
-- CATS is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.
--
-- CATS is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
--
-- You should have received a copy of the GNU General Public License
-- along with CATS.  If not, see <http://www.gnu.org/licenses/>.
--

--Created using /video-recorder-service/src/test/java/com/comcast/cats/recorder/persistence/SqlTableCreator.java
drop table pvr_media_info cascade constraints;
drop table pvr_recording cascade constraints;
drop table pvr_recording_status cascade constraints;
drop sequence hibernate_sequence;
create table pvr_media_info (id number(10,0) not null, created_time timestamp, file_path varchar2(255 char), http_path varchar2(255 char), last_updated_time timestamp, primary key (id));
create table pvr_recording (id number(10,0) not null, created_time timestamp, last_updated_time timestamp, mrl varchar2(255 char), stb_mac_address varchar2(255 char), video_server_ip varchar2(255 char), video_server_port number(10,0), primary key (id));
create table pvr_recording_status (id number(10,0) not null, message varchar2(255 char), status_code varchar2(255 char), primary key (id));
alter table pvr_media_info add constraint FKE3022B1C57118F1D foreign key (id) references pvr_recording;
create sequence hibernate_sequence;
