import axiosConfig from './axiosConfig';

const employeeService = {

    getTicketsByEmployee: async (accountId: number) => {
        const response = await axiosConfig.get(`/ticket/${accountId}`);
        return response.data;
    },


    submitTicket: async (ticket: { amount: number; description: string; createdBy: number }) => {
        const response = await axiosConfig.post('/ticket/submit', ticket);
        return response.data;
    },
};

export default employeeService;