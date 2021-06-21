import { IbanStatusPipe } from './iban-status.pipe';

describe('IbanStatusPipe', () => {
  it('create an instance', () => {
    const pipe = new IbanStatusPipe();
    expect(pipe).toBeTruthy();
  });
});
