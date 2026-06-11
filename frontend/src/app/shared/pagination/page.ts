export interface Page<T> {
    content: T[],
    first: boolean,
    last: boolean,
    totalPages: number,
    totalElements: number,
    number: number,
    numberOfElements: number
}