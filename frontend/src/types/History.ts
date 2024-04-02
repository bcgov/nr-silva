type Step = {
  step: number;
  status: string;
  description: string;
  subtitle: string;
}

type History = {
  id: number;
  steps: Step[];
}

export default History;
