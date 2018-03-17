package uk.ac.ebi.submission.store.submissionDocument;

public enum ProcessingStatus {
    Draft, /* exists in the usi system */
    Submitted, /* user has Submitted submissionDocument */
    Dispatched, /* usi has dispatched object to archive */
    Received, /* archive has received object */
    Curation, /* archive is curating object */
    ActionRequired, /* curator has requested changes from user*/
    Accepted, /* archive has accepted object */
    Processing, /* archive is processing object */
    Completed, /* archive has processed object */
    Rejected, /* archive has rejected the data because is unsuitable*/
    Error /* archive has rejected the submitted submissionDocument because it should not have been acceptted by the validation system*/

}
