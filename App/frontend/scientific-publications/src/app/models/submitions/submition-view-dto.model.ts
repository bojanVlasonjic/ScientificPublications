import { AuthorDto } from '../author-dto.model';

export class SubmitionViewDto {
    submitionId: number;
    paperId: string;
    paperTitle: string;
    authors: Array<AuthorDto>;
    status: string;

    constructor() {
    }
}