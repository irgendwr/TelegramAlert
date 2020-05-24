const isNumber = (possibleNumber) => {
  return possibleNumber !== '' && !isNaN(possibleNumber);
}

const FormUtils = {
  getValueFromInput(input) {
    switch (input.type) {
      case 'radio':
        const { value } = input;
        return (value === 'true' || value === 'false' ? value === 'true' : value);
      case 'checkbox':
        return input.checked;
      case 'number':
        return (input.value === '' || !isNumber(input.value) ? undefined : Number(input.value));
      default:
        return input.value;
    }
  },
};

export default FormUtils;
