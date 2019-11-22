import { IProgram } from 'app/shared/model/program.model';

export interface ITemat {
  id?: number;
  nazwa?: string;
  program?: IProgram;
}

export class Temat implements ITemat {
  constructor(public id?: number, public nazwa?: string, public program?: IProgram) {}
}
