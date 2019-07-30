const axios = require("axios");

class FileService {
  static BACKEND = "http://localhost:8080/files";

  async list(page, pageSize,searchFilter = null) {
    try {
      let url = FileService.BACKEND;
      const params = { page, size: pageSize };
      if (searchFilter){
        url += "/search/findByDescriptionOrTitle";
        params.searchTerm = searchFilter;
      }

      const response = await axios.get(url, { params });
      const { data } = response;

      return {
        page: data.page,
        files: data._embedded.files.map(file => {
          return { ...file, dateCreated: new Date(file.dateCreated) };
        })
      };
    } catch (error) {
      console.error(error);
      return {
        error
      };
    }
  }

  async removeFile(fileDetails) {
    const selfLink = fileDetails._links.self.href;
    await axios.delete(selfLink);
    return true;
  }
}

export default FileService;
