Geological Project

Description

A project for managing geological classes and sections. 
Implements REST CRUD API for working with data and has functionality for importing and exporting data in XLS format.

Requirements

Java 17
PostgreSQL

Installation

Clone the repository: 
git clone https://github.com/YOUR_USERNAME/geological-project.git

Fill in the configuration files, for example, application.property

Usage

CRUD API for Sections and GeologicalClasses

Get all Sections
GET /api/sections

Get Section by ID
GET /api/sections/{sectionId}

Get Sections by GeologicalClass code
GET /api/sections/by-code?code=...

Create a new Section
POST /api/sections
Request Body:
{
  "name": "Section 1",
  "geologicalClasses": [
    { "name": "Geo Class 11", "code": "GC11" },
    { "name": "Geo Class 12", "code": "GC12" }
  ]
}

Update a Section
PUT /api/sections/{sectionId}
Request Body:
{
  "name": "Updated Section",
  "geologicalClasses": [
    { "name": "Updated Geo Class 1", "code": "UGC1" },
    { "name": "Updated Geo Class 2", "code": "UGC2" }
  ]
}

Delete a Section
DELETE /api/sections/{sectionId}

Import and Export XLS Files
Start importing a file
POST /api/import (file)

Get import status by ID
GET /api/import/{id}

Start exporting data
GET /api/export

Get export status by ID
GET /api/export/{id}

Get export file by ID
GET /api/export/{id}/file
