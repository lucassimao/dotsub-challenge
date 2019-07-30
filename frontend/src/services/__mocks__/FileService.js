export const mockFileServiceList = jest.fn((page, pageSize) => {
  const backendMockedResponse = {
    _embedded: {
      files: []
    },
    page: {
      size: pageSize,
      totalElements: 100,
      totalPages: 100 / pageSize,
      number: page
    }
  };

  for (let i = 0; i < pageSize; ++i) {
    backendMockedResponse._embedded.files.push({
      name: `File ${i} - Page ${page}`,
      description: `Description for file ${i}`,
      dateCreated: new Date()
    });
  }

  return new Promise((resolve, reject) => {
    setTimeout(() => {
      resolve({
        files: backendMockedResponse._embedded.files,
        page: backendMockedResponse.page
      });
    }, 2000);
  });
});

const mock = jest.fn().mockImplementation(() => {
  return { list: mockFileServiceList };
});

export default mock;
