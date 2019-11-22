import { ITemat } from 'app/shared/model/temat.model';

export interface IProgram {
  id?: number;
  nazwa?: string;
  opis?: string;
  temats?: ITemat[];
}

export class Program implements IProgram {
  constructor(public id?: number, public nazwa?: string, public opis?: string, public temats?: ITemat[]) {}
}
