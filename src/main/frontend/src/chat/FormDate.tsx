const formatDate = (dateString: string | undefined): string => {
  if (!dateString) {
    return ""; // 또는 다른 기본값을 반환하거나 에러 처리를 수행할 수 있습니다.
  }

  const date = new Date(dateString);
  const formattedMonth = (date.getMonth() + 1).toString().padStart(2, "0");
  const formattedDay = date.getDate().toString().padStart(2, "0");
  const formattedHours = date.getHours().toString().padStart(2, "0");
  const formattedMinutes = date.getMinutes().toString().padStart(2, "0");

  return `${formattedMonth}/${formattedDay} ${formattedHours}:${formattedMinutes}`;
};

const formatDateArray = (regDateArray: any) => {
  const [year, month, day, hours, minutes] = regDateArray;

  const formattedMonth = month.toString().padStart(2, "0");
  const formattedDay = day.toString().padStart(2, "0");
  const formattedHours = hours.toString().padStart(2, "0");
  const formattedMinutes = minutes.toString().padStart(2, "0");

  return `${formattedMonth}/${formattedDay} ${formattedHours}:${formattedMinutes}`;
};

export { formatDate, formatDateArray };
