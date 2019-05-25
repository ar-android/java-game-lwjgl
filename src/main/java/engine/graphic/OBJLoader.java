package engine.graphic;

import engine.Utils;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class OBJLoader {

    public static Mesh loadMesh(String fileName) throws Exception {
        List<String> lines = Utils.readAllLines(fileName);

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Face> faces = new ArrayList<>();

        for (String line : lines) {
            String[] tokens = line.split("\\s+");
            switch (tokens[0]){
                case "v":
                    Vector3f vec3f = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    vertices.add(vec3f);
                    break;
                case "vt":
                    Vector2f vec2f = new Vector2f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2])
                    );
                    textures.add(vec2f);
                    break;
                case "vn":
                    Vector3f vec3fNorm = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    normals.add(vec3fNorm);
                    break;
                case "f":
                    Face face = new Face(tokens[1], tokens[2], tokens[3]);
                    faces.add(face);
                default:
                    break;
            }
        }
        return reorderLists(vertices, textures,normals, faces);
    }

    private static Mesh reorderLists(List<Vector3f> vertices, List<Vector2f> textures, List<Vector3f> normals, List<Face> faces) {
        List<Integer> indices = new ArrayList<>();
        float[] posArr = new float[vertices.size() * 3];
        int i = 0;
        for (Vector3f pos : vertices) {
            posArr[i * 3] = pos.x;
            posArr[i * 3 + 1] = pos.y;
            posArr[i * 3 + 2] = pos.z;
            i++;
        }
        float[] textCoordArr = new float[vertices.size() * 2];
        float[] normArr = new float[vertices.size() * 3];

        for (Face face : faces) {
            IdxGroup[] faceIndices = face.getFaceVertexIndices();
            for (IdxGroup idxValue : faceIndices) {
                processFaceVertex(idxValue, textures,normals, indices,
                        textCoordArr, normArr);
            }
        }
        int[] indicesArr = new int[indices.size()];
        indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();
        Mesh mesh = new Mesh(posArr, textCoordArr, normArr, indicesArr);
        return mesh;
    }

    private static void processFaceVertex(IdxGroup indices, List<Vector2f> texCoordList, List<Vector3f> normals, List<Integer> indicesList, float[] textCoordArr, float[] normArr) {

        int posIndex = indices.idxPos;
        indicesList.add(posIndex);

        if (indices.idxTexCoord >= 0) {
            Vector2f texCoord = texCoordList.get(indices.idxTexCoord);
            textCoordArr[posIndex * 2] = texCoord.x;
            textCoordArr[posIndex * 2 + 1] = 1 - texCoord.y;
        }

        if (indices.idxVecNormal >= 0) {
            Vector3f vecNorm = normals.get(indices.idxVecNormal);
            normArr[posIndex * 3] = vecNorm.x;
            normArr[posIndex * 3 + 1] = vecNorm.y;
            normArr[posIndex * 3 + 2] = vecNorm.z;
        }
    }

    protected static class Face {

        /**
         * List of idxGroup groups a face triangle (3 vertices per face)
         */
        private IdxGroup[] idxGroups = new IdxGroup[3];

        public Face(String v1, String v2, String v3) {
            idxGroups = new IdxGroup[3];
            idxGroups[0] = parseLine(v1);
            idxGroups[1] = parseLine(v2);
            idxGroups[2] = parseLine(v3);
        }

        public IdxGroup parseLine(String line) {
            IdxGroup idxGroup = new IdxGroup();

            String[] lineTokens = line.split("/");
            int length = lineTokens.length;
            idxGroup.idxPos = Integer.parseInt(lineTokens[0]) - 1;
            if (length > 1) {
                String texCoord = lineTokens[1];
                idxGroup.idxTexCoord = texCoord.length() > 0 ? Integer.parseInt(texCoord) - 1 : IdxGroup.NO_VALUE;
                if (length > 2){
                    idxGroup.idxVecNormal = Integer.parseInt(lineTokens[2]) - 1;
                }
            }

            return idxGroup;
        }

        public IdxGroup[] getFaceVertexIndices() {
            return idxGroups;
        }
    }

    protected static class IdxGroup {
        public static final int NO_VALUE = -1;
        public int idxPos;
        public int idxTexCoord;
        public int idxVecNormal;

        public IdxGroup() {
            idxPos = NO_VALUE;
            idxTexCoord = NO_VALUE;
            idxVecNormal = NO_VALUE;
        }
    }
}
