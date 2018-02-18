# subs-submission-store
playing around with an alternate architecture for subs repository and API

## Ideas
* Remove all database access from other components. Use the API.
  * Difference between user changes (to document or status), vs system updates (validation, status)
  * Can we differentiate between user / non-user updates within a validator
* Model all submittables as JSON documents within a single mongo collection
* Embed all state and identity information within that collection
* Embed submission state with the submission
* Move all status configuration with status mongo collections
* Track long term submittable state (accession, archive name, release status) in a separate mongo collection
* Create a 'backend for front end' as a proxy for this API, for end users
  * Therefore, need a client library
* Replace fixed classes with a collection containing JSON schema, to be used in the async validation phase
  * Therefore, need a validator based on schema
* Extract key refs (files, other submittables) from the submitted document, embed in submittable collection, use for simplified validation (schema should specify you need the ref, we extract and normalise for validation)
  * Therefore, need a validator that uses this information
* Archives can model their documents anyway they like (within reason), we validate on these criteria:
  * Matches schema
  * File refs can be matched to uploaded files
  * Refs to other submittables can be resolved
  * Passes custom validation provided by archive
* Use schema to drive UI
  * work to do rendering schema driven form into table
  * work to do translating UI form to spreadsheet
* Validation system remains intact, with some changes to how versions are tracked and results recorded
* Dispatch system remains intact, but switches to generalised model, less specific envelope structure
  
  



 
 
