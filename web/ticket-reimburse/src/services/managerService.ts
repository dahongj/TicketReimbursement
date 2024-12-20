import axiosConfig from './axiosConfig';

const managerService = {
    getPendingTickets: async () => {
        const response = await axiosConfig.get('/ticket/pending');
        return response.data;
    },

    processTicket: async (ticket: { id: number; status: string; [key: string]: any }) => {
        const response = await axiosConfig.patch(`/ticket/process/${ticket.id}`, ticket);
        return response.data;
    },

    getAllUsers: async () => {
        const response = await axiosConfig.get('/manager/allusers');
        return response.data;
    },

    updateUserRole: async (user: { accountId: number; username: string; role: string }) => {
        const response = await axiosConfig.patch(`/manager/editrole/${user.accountId}`, user);
        return response.data;
    },
};

export default managerService;