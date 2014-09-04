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

ALTER TABLE pvr_media_metadata DROP FOREIGN KEY FKE3022B1C57118F1D;

DROP TABLE IF EXISTS pvr_media_metadata;
DROP TABLE IF EXISTS pvr_recording;
DROP TABLE IF EXISTS pvr_recording_status;

-- --------------------------------------------------------
-- Media Information TABLE
-- Notes: This table metadata of individual recordings.
-- --------------------------------------------------------
CREATE TABLE pvr_media_metadata 
(
	media_metadata_id 					 integer NOT NULL AUTO_INCREMENT, 
	file_path            varchar(255),           -- Absolute file path.
	http_path            varchar(255),           -- Absolute http url.
	parent_id 			integer NULL DEFAULT NULL, 
-- STANDARD audit columns
-- -----------------------	
	created_time         timestamp    NOT NULL DEFAULT '0000-00-00 00:00:00',                   
	last_updated_time    timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 

-- PRIMARY KEY  Constraint
-- ----------------------- 	
	PRIMARY KEY (media_metadata_id)
)
AUTO_INCREMENT=1 ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- --------------------------------------------------------
-- Recording log TABLE
-- Notes: This table stores of all available recordings.
-- -------------------------------------------------------
CREATE TABLE pvr_recording 
(
	recording_id 		 integer NOT NULL AUTO_INCREMENT,
	recording_name       varchar(100),           -- Alias
	stb_mac_address 	 varchar(20),            -- Settop host mac address                                   
	video_server_ip 	 varchar(20),            -- IP
	video_server_port    integer,                -- Port
	mrl 				 varchar(255),           -- Media resource Locator	
	requested_duration   integer,                -- Initially requested duration
	
-- STANDARD audit columns
-- -----------------------	
	created_time         timestamp    NOT NULL DEFAULT '0000-00-00 00:00:00',                   
	last_updated_time    timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	
-- PRIMARY KEY  Constraint
-- ----------------------- 
	PRIMARY KEY (recording_id)
)
AUTO_INCREMENT=1 ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------
-- Recording status TABLE
-- Notes: This table stores status of individual recordings.
-- --------------------------------------------------------
CREATE TABLE pvr_recording_status 
(
	recording_status_id 					integer NOT NULL AUTO_INCREMENT, 
	message 			varchar(500), 
	state 		        varchar(20), 
-- PRIMARY KEY  Constraint
-- ----------------------- 	
	PRIMARY KEY (recording_status_id)
)
AUTO_INCREMENT=1 ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- FOREIGN KEY  Constraint
-- -----------------------  
ALTER TABLE pvr_media_metadata ADD CONSTRAINT FKE3022B1C57118F1D FOREIGN KEY (parent_id) REFERENCES pvr_recording (recording_id);
