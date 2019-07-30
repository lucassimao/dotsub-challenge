const axios = require("axios");

class FileService {
  static BACKEND = "http://localhost:8080/files";

  async list(page, pageSize, searchFilter = null) {
    try {
      let url = FileService.BACKEND;
      const params = { page, size: pageSize, sort: "dateCreated,desc" };
      if (searchFilter) {
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

  async update(
    resourceEndpoint,
    title,
    description,
    base64EncodedFile,
    name,
    mimeType
  ) {
    try {
      const response = await axios.put(resourceEndpoint, {
        title,
        description,
        data: base64EncodedFile,
        mimeType,
        originalFileName: name
      });
      return response.status === 201;
    } catch (error) {
      if (error.response) {
        console.log(error.response);
      } else if (error.request) {
        console.log(error.request);
      } else {
        console.log("Error", error.message);
      }
      return false;
    }
  }

  async save(title, description, base64EncodedFile, name, mimeType) {
    try {
      const response = await axios.post(FileService.BACKEND, {
        title,
        description,
        data: base64EncodedFile,
        mimeType,
        originalFileName: name
      });
      return response.status === 201;
    } catch (error) {
      if (error.response) {
        console.log(error.response);
      } else if (error.request) {
        console.log(error.request);
      } else {
        console.log("Error", error.message);
      }
      return false;
    }
  }
}

export default FileService;
