
export class SearchParams {

    query: string;
    dateCreated: string;
    datePublished: string;
    dateRevised: string;
    status: string;
    customMetaData: string;

    constructor() {
        this.query = '';
        this.dateCreated = '';
        this.datePublished = '';
        this.dateRevised = '';
        this.status = '';
        this.customMetaData = '';
    }

}