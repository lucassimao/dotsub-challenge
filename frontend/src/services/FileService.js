const axios = require("axios");

class FileService {
  static BACKEND = "http://localhost:8080/files";

  async list(page, pageSize) {
    try {
        const params = { page, size:pageSize }
      const response = await axios.get(FileService.BACKEND, {params});
      const {data} = response;
      
      return {
        page: data.page,
        files: data._embedded.files
      };
    } catch (error) {
      console.error(error);
      return {
        error
      };
    }
  }
}

export default FileService;
